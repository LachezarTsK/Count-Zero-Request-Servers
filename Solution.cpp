
#include <span>
#include <ranges>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {

    struct QueryPair {
            int index{};
            int time{};
            QueryPair(int index, int time) :index{ index }, time{ time } {}
    };

public:
    vector<int> countServers(int numberOfServers, vector<vector<int>>& logs, int timeRange, vector<int>& queries) {
        vector<QueryPair> queryPairs;
        queryPairs.reserve(queries.size());
        for (int i = 0; i < queries.size(); ++i) {
            queryPairs.emplace_back(i, queries[i]);
        }

        ranges::sort(queryPairs, [](const auto& first, const auto& second) {return first.time < second.time; });
        ranges::sort(logs, [](const auto& first, const auto& second) {return first[1] < second[1]; });

        return findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers, logs, timeRange, queryPairs);
    }

private:
    vector<int> findNumberOfServersNotReceivingRequestsPerQuery(int numberOfServers, span<const vector<int>> logs, int timeRange, span<const QueryPair> queryPairs) {
        int front = 0;
        int back = 0;
        int countServersReceivingRequest = 0;
        vector<int> frequencyPerQueryForServerID(numberOfServers + 1);
        vector<int> numberOfServersNotReceivingRequestsPerQuery(queryPairs.size());

        for (int i = 0; i < queryPairs.size(); ++i) {
            while (front < logs.size() && logs[front][1] <= queryPairs[i].time) {
                if (frequencyPerQueryForServerID[logs[front][0]] == 0) {
                    ++countServersReceivingRequest;
                }
                ++frequencyPerQueryForServerID[logs[front][0]];
                ++front;
            }

            while (back < logs.size() && logs[back][1] < queryPairs[i].time - timeRange) {
                if (frequencyPerQueryForServerID[logs[back][0]] == 1) {
                    --countServersReceivingRequest;
                }
                --frequencyPerQueryForServerID[logs[back][0]];
                ++back;
            }
            numberOfServersNotReceivingRequestsPerQuery[queryPairs[i].index] = numberOfServers - countServersReceivingRequest;
        }

        return numberOfServersNotReceivingRequestsPerQuery;
    }
};
