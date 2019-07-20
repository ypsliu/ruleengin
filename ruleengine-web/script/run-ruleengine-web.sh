#!/usr/bin/env bash
nohup java -jar -DAPP_HOME="." -Dspring.profiles.active=standalonenocache ./ruleengine-web-1.0.0.jar >/dev/null 2>&1 &
