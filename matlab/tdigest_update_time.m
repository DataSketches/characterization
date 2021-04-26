# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

clf;

td_200=load('../results/tdigest_sketch_timing_200.tsv');

semilogx(td_200(:,1), td_200(:,4));

set(gca, 'fontsize', 16);
title 'Update time of t-digest sketch compression=200'
xlabel 'stream size'
legend('t-digest 200', 'location', 'southeast');
ylabel 'update time, nanoseconds'
grid minor on
