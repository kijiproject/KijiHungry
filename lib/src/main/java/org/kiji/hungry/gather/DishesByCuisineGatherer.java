package com.wibidata.hungry.gather;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.wibidata.core.client.MapReduceJob;
import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiConfiguration;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiGatherJobBuilder;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.WibiTable;
import com.wibidata.core.client.lib.gather.LabelNodeGatherer;
import com.wibidata.core.client.lib.graph.NodeBuilder;
import com.wibidata.core.client.lib.output.WibiColumnMapReduceJobOutput;
import com.wibidata.hungry.reduce.KeepAnnotationsMergeNodeReducer;

public class DishesByCuisineGatherer extends LabelNodeGatherer implements Tool {
  public static enum Counters {
    MISSING_FIELDS,
    CUISINE_EMPTY
  }

  @Override
  public WibiDataRequest getDataRequest() {
    return new WibiDataRequest()
        .addColumn(new WibiDataRequest.Column("info", "id"))
        .addColumn(new WibiDataRequest.Column("info", "name"))
        .addColumn(new WibiDataRequest.Column("info", "description"))
        .addColumn(new WibiDataRequest.Column("info", "cuisine"))
        .addColumn(new WibiDataRequest.Column("info", "thumbnail"));
  }

  @Override
  public void gather(WibiRowData input, Context context) throws IOException, InterruptedException {
    if (!input.containsColumn("info", "id") || !input.containsColumn("info", "cuisine")
        || !input.containsColumn("info", "description") || !input.containsColumn("info", "name")
        || !input.containsColumn("info", "thumbnail")) {
      context.getCounter(Counters.MISSING_FIELDS).increment(1);
      return;
    }

    if (input.getStringValue("info", "cuisine").toString().isEmpty()) {
      context.getCounter(Counters.CUISINE_EMPTY).increment(1);
      return;
    }

    NodeBuilder node = new NodeBuilder("cuisine:" + input.getStringValue("info", "cuisine"));
    node.addEdge().target(input.getStringValue("info", "id"))
        .addAnnotation("name", input.getStringValue("info", "name"))
        .addAnnotation("description", input.getStringValue("info", "description"))
        .addAnnotation("thumbnail", input.getStringValue("info", "thumbnail"));
    write(node.build(), context);
  }

  @Override
  public int run(String[] args) throws Exception {
    Wibi wibi = Wibi.open(
        new WibiConfiguration(getConf(), WibiConfiguration.DEFAULT_INSTANCE_NAME));
    WibiTable dishTable = WibiTable.open(wibi, "wibi_hungry_dish");
    WibiTable categoryTable = WibiTable.open(wibi, "wibi_hungry_category");

    try {
      MapReduceJob job = new WibiGatherJobBuilder()
          .withInputTable(dishTable)
          .withGatherer(DishesByCuisineGatherer.class)
          .withReducer(KeepAnnotationsMergeNodeReducer.class)
          .withOutput(new WibiColumnMapReduceJobOutput(categoryTable, "related", "dish", 1))
          .build();
      return job.run() ? 0 : 1;
    } finally {
      IOUtils.closeQuietly(categoryTable);
      IOUtils.closeQuietly(dishTable);
      IOUtils.closeQuietly(wibi);
    }
  }

  public static void main(String[] args) throws Exception {
    System.exit(ToolRunner.run(HBaseConfiguration.create(), new DishesByCuisineGatherer(), args));
  }
}
