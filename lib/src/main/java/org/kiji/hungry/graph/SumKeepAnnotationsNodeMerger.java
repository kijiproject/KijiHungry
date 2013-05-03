package com.wibidata.hungry.graph;

import java.util.Map;

import com.wibidata.core.client.AvroMapReader;
import com.wibidata.core.client.avro.Node;
import com.wibidata.core.client.lib.graph.SumNodeMerger;

public class SumKeepAnnotationsNodeMerger extends SumNodeMerger {
  @Override
  protected void update(Node mergedNode, Node newNode) {
    super.update(mergedNode, newNode);

    // Also copy over the annotations.
    if (null != newNode.getAnnotations()) {
      if (null == mergedNode.getAnnotations()) {
        // Copy over all the annotations as-is.
        mergedNode.setAnnotations(newNode.getAnnotations());
      } else {
        // Copy over only the annotations that aren't already set.
        AvroMapReader<CharSequence> mergedAnnotations =
            new AvroMapReader<CharSequence>(mergedNode.getAnnotations());
        for (Map.Entry<CharSequence, CharSequence> annotation
                 : newNode.getAnnotations().entrySet()) {
          if (!mergedAnnotations.containsKey(annotation.getKey())) {
            mergedAnnotations.put(annotation.getKey(), annotation.getValue());
          }
        }
      }
    }
  }
}
