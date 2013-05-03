package com.wibidata.hungry.reduce;

import java.io.IOException;

import com.wibidata.core.client.lib.reduce.MergeNodeReducer;
import com.wibidata.hungry.graph.SumKeepAnnotationsNodeMerger;

public class KeepAnnotationsMergeNodeReducer extends MergeNodeReducer {
  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
    mNodeMerger = new SumKeepAnnotationsNodeMerger();
  }
}
