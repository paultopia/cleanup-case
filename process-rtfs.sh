#!/usr/bin/env bash
for i in *.rtf
do
    lein run "$i"
done
