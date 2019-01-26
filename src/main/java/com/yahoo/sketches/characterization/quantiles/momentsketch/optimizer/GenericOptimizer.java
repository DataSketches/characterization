package com.yahoo.sketches.characterization.quantiles.momentsketch.optimizer;

public interface GenericOptimizer {
    void setVerbose(boolean flag);

    void setMaxIter(int maxIter);

    boolean isConverged();

    int getStepCount();

    FunctionWithHessian getP();

    double[] solve(double[] start, double gradTol);
}