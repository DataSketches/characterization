clf;

fi_str_cp_sh=load('../results/frequent_items_merge_timing_string_lgk10_zipf11_short_copy.tsv');
fi_str_mv_sh=load('../results/frequent_items_merge_timing_string_lgk10_zipf11_short_move.tsv');
fi_str_cp_ln=load('../results/frequent_items_merge_timing_string_lgk10_zipf11_long_copy.tsv');
fi_str_mv_ln=load('../results/frequent_items_merge_timing_string_lgk10_zipf11_long_move.tsv');

hold on;
semilogx(fi_str_cp_sh(:,1), fi_str_cp_sh(:,5), 'linewidth', 2);
semilogx(fi_str_mv_sh(:,1), fi_str_mv_sh(:,5), 'linewidth', 2);
semilogx(fi_str_cp_ln(:,1), fi_str_cp_ln(:,5), 'linewidth', 2);
semilogx(fi_str_mv_ln(:,1), fi_str_mv_ln(:,5), 'linewidth', 2);

set(gca, 'fontsize', 16);
title 'Merge time of frequent items sketch<std::string>(10)'
xlabel 'stream size'
ylabel 'time, nanoseconds'
legend('zipf 1.1 copy short strings', 'zipf 1.1 move short strings', 'zipf 1.1 copy long strings', 'zipf 1.1 move long strings', 'location', 'northwest');
grid minor on
