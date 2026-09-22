# fmapf - Fast Multi-Agent Path Finding with Monte Carlo Tree Search

This is a Java framework for multi-agent path finding (MAPF) and multi-agent task assignment using Monte Carlo Tree Search (MCTS). It is the latest and most complete version of this line of work.

## The problem

Several agents share a grid map, and each one has to reach a goal. The agents can't collide or overlap. The goal is to find paths for all of them that are good for the team as a whole, not just for each agent separately.

This framework treats the problem as a search over joint actions and solves it with MCTS. It supports two setups:

- **Centralized:** one planner decides for all agents.
- **Decentralized:** each agent plans on its own, without talking to the others.

## How it works

The solver is in `src/main/MonteCarloTreeSearch.java`. It runs the standard four MCTS steps:

1. **Selection:** go down the tree, picking children with the UCT rule.
2. **Expansion:** add new child states to the tree.
3. **Rollout:** simulate the rest of the episode to estimate a value.
4. **Backpropagation:** update the values and visit counts along the path back to the root.

Each problem type is a separate module that implements the `Game`, `State`, `Action`, `Board`, `Simulator` and `Value` interfaces. This keeps the solver independent of the problem.

| Module | Problem |
|---|---|
| `src/fmapf` | MAPF on standard benchmark maps |
| `src/prima` | Cooperative multi-agent task assignment |

The value models differ in how they reward and penalize conflicts between agents. You can run several of them on the same instance and compare the results.

## Benchmarks

`input/testcase/fmapf/` contains maps and scenarios from the MovingAI MAPF benchmark. These include:

- open grids: `empty-8-8` to `empty-48-48`
- city maps: `Berlin`, `Boston`, `Paris`
- game maps: `den312d`, `den520d`, `brc202d`
- random and even scenario files for each map

The other folders under `input/testcase/` hold test cases from the earlier versions of this project.

## Running

1. Import the `fmapf` folder into Eclipse or IntelliJ as a Java project.
2. Set up the run in `input/configuration.txt`:

```
   0                                           input mode
   001000                                      debug flags
   false                                       garbage collector on/off
   0000001                                     value models to run (one flag per model)
   FMAPF                                       problem: FMAPF or PRIMA
   mapf-map/empty-16-16.map                    map file
   scen-random/empty-16-16-random-4.scen       scenario file
   35                                          number of agents
   false                                       local solver
   true                                        centralized (true) or decentralized (false)
```

3. Run `main.Main`.

## Related papers

- M. S. Kiarostami, M. Daneshvaramoli, S. Khalaj Monfared, D. Rahmati, S. Gorgin. *Multi-Agent Non-Overlapping Pathfinding with Monte-Carlo Tree Search.* IEEE Conference on Games (CoG), 2019.
- M. Daneshvaramoli, M. S. Kiarostami, S. Khalaj Monfared, H. Karisani, H. Khashehchi, D. Rahmati, S. Gorgin, A. Rahmati. *Decentralized Cooperative Communication-less Multi-Agent Task Assignment with Monte-Carlo Tree Search.* [arXiv:1910.12062](https://arxiv.org/abs/1910.12062)

## Tech

Plain Java with no external dependencies.
