#!/usr/bin/env bash

rm -rf ./gen-java
thrift -gen java dto.thrift
thrift -gen java service.thrift
rm -rf ./../thrift/../main/java/
mv ./gen-java ./../thrift/../main/java/