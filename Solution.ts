
function countServers(numberOfServers: number, logs: number[][], timeRange: number, queries: number[]): number[] {
    const queryPairs = new Array(queries.length);
    for (let i = 0; i < queries.length; ++i) {
        queryPairs[i] = new QueryPair(i, queries[i]);
    }

    queryPairs.sort((first, second) => first.time - second.time);
    logs.sort((first, second) => first[1] - second[1]);

    return findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers, logs, timeRange, queryPairs);
};

class QueryPair {
    constructor(public index: number, public time: number) { }
}

function findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers: number, logs: number[][], timeRange: number, queryPairs: QueryPair[]): number[] {
    let front = 0;
    let back = 0;
    let countServersReceivingRequest = 0;
    const frequencyPerQueryForServerID = new Array(numberOfServers + 1).fill(0);
    const numberOfServersNotReceivingRequestsPerQuery = new Array(queryPairs.length).fill(0);

    for (let i = 0; i < queryPairs.length; ++i) {
        while (front < logs.length && logs[front][1] <= queryPairs[i].time) {
            if (frequencyPerQueryForServerID[logs[front][0]] === 0) {
                ++countServersReceivingRequest;
            }
            ++frequencyPerQueryForServerID[logs[front][0]];
            ++front;
        }

        while (back < logs.length && logs[back][1] < queryPairs[i].time - timeRange) {
            if (frequencyPerQueryForServerID[logs[back][0]] === 1) {
                --countServersReceivingRequest;
            }
            --frequencyPerQueryForServerID[logs[back][0]];
            ++back;
        }
        numberOfServersNotReceivingRequestsPerQuery[queryPairs[i].index] = numberOfServers - countServersReceivingRequest;
    }

    return numberOfServersNotReceivingRequestsPerQuery;
}
