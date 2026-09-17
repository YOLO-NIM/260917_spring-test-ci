FROM ubuntu:latest
LABEL authors="c"

ENTRYPOINT ["top", "-b"]