# Assignment 5 - Activity 1

# Project Explanation

I based my project on the PeerToPeer example in the Sockets directory, as suggested on Slack. I kept almost all of the core functionality unchanged. I added more attributes and operations to the Peer class to allow for behavior fulfilling each of the Leader, Node, and Client roles. I also added more branches in the request and response while loops in Peer and ServerTask, respectively.

Each Peer has a money field for tracking its current funds, and a Hashmap of Client IDs and debts, both of which would be used for implementing the consensus requirements.

# Protocol Explanation

My protocol was also virtually unchanged from the provided example. It was definitely not the most efficient way, but a specific JSON object was built as a string for each branch in the respective loops, with the universal attribute being a type field to trigger the appropriate branch.

## Requirements Met

1. README

- screencast link:
- Explanation of project/completed requirements: done
- Explain protocol: done

2. Project structure/ease of comprehension: I kept the overall structure almost exactly the same as the original example code, so I believe it is very straightforward and easy to understand.
3. "gradle leader" task: done
4. "gradle node1" and "gradle node2" tasks: done. Extra 3 pts: technically, many nodes can join, but they don't really do their required tasks.
5. Default node money and optional parameter: done
6. "gradle client" task: done
7. Leader requests Client ID/Client responds: done
8. Client choose option/amount: not done/partial. I was able to have the client display the menu, and had the amount entry and message sending set up, but the Leader thread got hung up at the same point and I could not figure out the problem.
9. Leader receives request/amount: not done
10. Credit peration: not done
11. Pay ack peration: not done
12. Node crash resilience: not done
13. System/transaction persistence: not done
14. Multiple clients: not done
15. New nodes: technically. Many nodes can join, since everything is a Peer, but I don't want to count this on a technicality since most of the node's core functions don't work.

### Running the Project

Leader should be started first, then however many Nodes you would like to run, then Client.

- Running Leader: gradle leader
- Running node1: gradle node1 -or- gradle node1 -Pmoney=1000
- Running node2: gradle node2 -or- gradle node1 -Pmoney=1000
- Running additional nodes: gradle runPeer -PpeerName="node3" -Ppeer=localhost:9003
- Running Client: gradle client

The leaderAddr. clientAddr, isLeader, and isClient parameters are always hardcoded, so you never need to enter those. Just the peerName, peer, and money parameters are needed for creating additional nodes or setting custom starting balances.
