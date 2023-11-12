#!/bin/bash

helm install prometheus prometheus-community/prometheus --version 23.4.0 -f values.yaml
