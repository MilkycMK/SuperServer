FROM ubuntu:latest
LABEL authors="milkyc"

ENTRYPOINT ["top", "-b"]