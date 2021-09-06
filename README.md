# Software Engineering Project A.A. 2020-2021
Progetto di Ingegneria del Software A.A. 2020-2021
Java implementation of the table game Masters of Renaissance by Cranio Creations as part of the Bachelor thesis.

Professor: Prof. Pierluigi San Pietro

Group: PSP28

Students:
- Roberto Neglia (immatriculation number: 10666415)
- Luca Cellamare (immatriculation number: 10491481)
- Antonio Massa (immatriculation number: 10587797)

## Implemented features

| Feature | Implemented |
| ------- | ----------- |
| All the rules ("Regole complete") | :heavy_check_mark: |
| CLI | :heavy_check_mark: |
| GUI | :heavy_check_mark: |
| Socket | :heavy_check_mark: |
| Advanced functionality 1 (FA 1) | :heavy_check_mark: Resilience to disconnections ("Resilienza alle disconnessioni") |

## Development

The software has been written using [Java SE 14](https://docs.oracle.com/en/java/javase/14/).

The IDE used for the development is [IntelliJ Idea](https://www.jetbrains.com/idea/) 2021.1.

## Testing

Thanks to unit tests and manual inspection, extensive testing was performed on all parts of the software.
We covered 92% of the lines of the model with unit tests. The few non-covered lines mostly are related to getter/setter methods and runtime exceptions.

## Compile

To run the tests and compile the software:

1. Install [Java SE 14](https://docs.oracle.com/en/java/javase/14/)
2. Install [Maven](https://maven.apache.org/install.html)
3. Clone this repo
4. In the cloned repo folder, run:
```bash
mvn package
```

5. The compiled artifacts (`Client.jar`) and (`Server.jar`) will be inside the `target` folder.

## Quick start guide

The following commands are meant to be run inside the `target` folder.

## To run the server:

1. In a terminal window, run:
```bash
java -jar Server.jar
```

## To run the client:
1. To start a GUI client in a terminal window, run:
```bash
java -jar Client.jar --GUI
```

2. To start a CLI client in a terminal window, run:
```bash
java -jar Client.jar --CLI
```
