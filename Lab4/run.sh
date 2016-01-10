#!/bin/bash
clear
echo "Running Secure Voting Machine"
java -cp CTF CTF & java -cp CLA CLA & java -cp Client GUI 
