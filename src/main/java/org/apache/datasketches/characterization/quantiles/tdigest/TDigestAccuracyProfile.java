package org.apache.datasketches.characterization.quantiles.tdigest;

import java.util.Arrays;

import org.apache.datasketches.Properties;
import org.apache.datasketches.characterization.quantiles.tdigest.DataGenerator.Mode;

import com.tdunning.math.stats.TDigest;

public class TDigestAccuracyProfile extends QuantilesAccuracyProfile {

  private int compression;
  private double[] inputValues;
  private DataGenerator gen;
  private DoubleRankCalculator.Mode rankMode;

  @Override
  void configure(final Properties props) {
    compression = Integer.parseInt(props.mustGet("compression"));
    gen = new DataGenerator(Mode.valueOf(props.mustGet("data")));
    rankMode = DoubleRankCalculator.Mode.valueOf(props.mustGet("rank"));
  }

  @Override
  void prepareTrial(final int streamLength) {
    inputValues = new double[streamLength];
  }

  @Override
  double doTrial() {
    gen.fillArray(inputValues);

    // build sketch
    final TDigest sketch = TDigest.createDigest(compression);
    for (int i = 0; i < inputValues.length; i++) {
      sketch.add(inputValues[i]);
    }

    Arrays.sort(inputValues);

    // query sketch and gather results
    double maxRankError = 0;
    final DoubleRankCalculator rank = new DoubleRankCalculator(inputValues, rankMode);
    for (int i = 0; i < inputValues.length; i++) {
      final double trueRank = rank.getRank(inputValues[i]);
      final double estRank = sketch.cdf(inputValues[i]);
      maxRankError = Math.max(maxRankError, Math.abs(trueRank - estRank));
    }
    return maxRankError;
  }

  @Override
  public void shutdown() {
    // TODO Auto-generated method stub

  }

  @Override
  public void cleanup() {
    // TODO Auto-generated method stub

  }

}