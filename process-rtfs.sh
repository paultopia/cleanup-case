#!/usr/bin/env bash
for i in *.rtf
do
    ./cleancase "$i"
done
