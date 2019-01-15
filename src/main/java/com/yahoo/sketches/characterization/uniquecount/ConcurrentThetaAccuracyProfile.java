/*
 * Copyright 2018, Yahoo! Inc. Licensed under the terms of the
 * Apache License 2.0. See LICENSE file at the project root for terms.
 */

package com.yahoo.sketches.characterization.uniquecount;

import static com.yahoo.sketches.Util.DEFAULT_UPDATE_SEED;

import com.yahoo.memory.WritableDirectHandle;
import com.yahoo.memory.WritableMemory;
import com.yahoo.sketches.theta.Sketch;
import com.yahoo.sketches.theta.UpdateSketch;
import com.yahoo.sketches.theta.UpdateSketchBuilder;

/**
 * @author Lee Rhodes
 */
public class ConcurrentThetaAccuracyProfile extends BaseAccuracyProfile {
  private UpdateSketch sharedSketch;
  private UpdateSketch localSketch;
  private int sharedLgK;
  private int localLgK;
  private int cacheLimit;
  private boolean ordered;
  private boolean offHeap;
  private boolean rebuild; //Theta QS Sketch Accuracy
  private boolean sharedIsDirect;
  private WritableDirectHandle wdh;
  private WritableMemory wmem;


  @Override
  void configure() {
    //Configure Sketches
    sharedLgK = lgK;
    localLgK = Integer.parseInt(prop.mustGet("CONCURRENT_THETA_localLgK"));
    cacheLimit = Integer.parseInt(prop.mustGet("CONCURRENT_THETA_cacheLimit"));
    ordered = Boolean.parseBoolean(prop.mustGet("CONCURRENT_THETA_ordered"));
    offHeap = Boolean.parseBoolean(prop.mustGet("CONCURRENT_THETA_offHeap"));
    rebuild = Boolean.parseBoolean(prop.mustGet("CONCURRENT_THETA_rebuild"));
    sharedIsDirect = Boolean.parseBoolean(prop.mustGet("CONCURRENT_THETA_sharedIsDirect"));

    final int maxSharedUpdateBytes = Sketch.getMaxUpdateSketchBytes(1 << sharedLgK);

    if (offHeap) {
      wdh = WritableMemory.allocateDirect(maxSharedUpdateBytes);
      wmem = wdh.get();
    } else {
      wmem = WritableMemory.allocate(maxSharedUpdateBytes);
    }
    final UpdateSketchBuilder bldr = configureBuilder();
    //must build shared first
    sharedSketch = bldr.buildShared(wmem);
    localSketch = bldr.buildLocal(sharedSketch);
  }

  @Override
  void doTrial() {
    final int qArrLen = qArr.length;
    //reuse the same sketches
    sharedSketch.reset(); // reset shared sketch first
    localSketch.reset();  // local sketch reset is reading the theta from shared sketch
    int lastUniques = 0;
    for (int i = 0; i < qArrLen; i++) {
      final AccuracyStats q = qArr[i];
      final double delta = q.trueValue - lastUniques;
      for (int u = 0; u < delta; u++) {
        localSketch.update(++vIn);
      }
      lastUniques += delta;
      if (rebuild) { sharedSketch.rebuild(); } //Resizes down to k. Only useful with QSSketch
      q.update(sharedSketch.getEstimate());
      if (getSize) {
        q.bytes = sharedSketch.compact().toByteArray().length;
      }
    }
  }

  @Override
  public void cleanup() {
    if (wdh != null) {
      wdh.close();
    }
  }

  //configures builder for both local and shared
  UpdateSketchBuilder configureBuilder() {
    final UpdateSketchBuilder bldr = new UpdateSketchBuilder();
    bldr.setSharedLogNominalEntries(sharedLgK);
    bldr.setLocalLogNominalEntries(localLgK);
    bldr.setSeed(DEFAULT_UPDATE_SEED);
    bldr.setCacheLimit(cacheLimit);
    bldr.setPropagateOrderedCompact(ordered);
    bldr.setSharedIsDirect(sharedIsDirect);
    return bldr;
  }

}
