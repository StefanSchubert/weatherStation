#!/usr/bin/env bash
echo "copy build jar to container assets."
mkdir -p assets/opt/WeatherStation
cp ../../target/WeatherStation.jar assets/opt/WeatherStation
echo "done"
