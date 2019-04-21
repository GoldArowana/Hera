#!/usr/bin/env bash

thrift -gen java dto.thrift
thrift -gen java service.thrift