clf;
d1=load('../results/frequent_items_sketch_int64_lgk10_zipf07.tsv');
d2=load('../results/frequent_items_sketch_int64_lgk10_zipf1.tsv');
d3=load('../results/frequent_items_sketch_int64_lgk10_zipf11.tsv');
d4=load('../results/frequent_items_sketch_int64_lgk10_geom0005.tsv');
semilogx([d1(:,1), d2(:,1), d3(:,1), d4(:,1)], [d1(:,5), d2(:,5), d3(:,5), d4(:,5)], 'linewidth', 2);
set(gca, 'fontsize', 16);
title 'Serialize time of frequent items sketch<long long>(10) using stream'
xlabel 'n'
ylabel 'time, nanoseconds'
legend('zipf 0.7', 'zipf 1.0', 'zipf 1.1', 'geom 0.005', 'location', 'northwest');
grid minor on
