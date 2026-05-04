#!/bin/bash

#PORT as an argument
if [ -z "$1" ]; then
  echo "Error: Port argument is missing."
  echo "Usage: ./start.sh <PORT>"
  exit 1
fi

export PORT=$1

echo "Building and starting Stock Market Simulator on port $PORT..."

docker-compose up --build -d

echo "====================================================="
echo "Application successfully started!"
echo "API is available at: http://localhost:$PORT"
echo "====================================================="