#!/bin/sh
#
# Copyright (c) 2004-2013, Willem Cazander
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without modification, are permitted provided
# that the following conditions are met:
#
# * Redistributions of source code must retain the above copyright notice, this list of conditions and the
#   following disclaimer.
# * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
#   the following disclaimer in the documentation and/or other materials provided with the distribution.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
# EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
# THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
# OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
# HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
# TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
# SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#

# Config script
# note: 3.0.3++ as in prev version there are some java6 classes. 
MVN=~/bin/mvn3/apache-maven-3.0.3/bin/mvn;
MVN_ARG="clean test";
JVMS="
  /usr/lib/jvm/java-6-openjdk-amd64/
  /usr/lib/jvm/java-7-openjdk-amd64/
  /usr/lib/jvm/j2sdk1.6-oracle/
  /usr/lib/jvm/j2sdk1.7-oracle/
  /usr/lib/jvm/jdk1.5.0_22/"
#/usr/lib/jvm/java-1.5.0-gcj-4.7/

# Goto project root;
cd `dirname $0`/../../..;

# Print meta data
echo "Starting jvm tests";
echo "Maven cmd: $MVN";
echo "Maven arg: $MVN_ARG";

# Check conditions
if [ "" != "$1" ]; then
  echo "Starting single jvm test;";
  JVMS=$1;
else
  echo "Starting jvm tests;";
fi;
echo "";

# Run tests per jvm
for JVM in $JVMS; do
  JVM_KEY=`echo $JVM | sed 's/\/\|\-//g'|tr -d '.'`;
  echo "Running in jvm: $JVM";
  if [ -e $JVM ]; then
    export JAVA_HOME=$JVM;
    $MVN $MVN_ARG;
    RESULT=$?;
  else
    RESULT="JVM path not found";
  fi;
  
  export "JVM_RESULT""$JVM_KEY"="$RESULT";
done;

EXIT=0;
echo "";
echo "Test summary;";
for JVM in $JVMS; do
  JVM_KEY=`echo $JVM | sed 's/\/\|\-//g'|tr -d '.'`;
  RESULT=`eval echo \\${"JVM_RESULT""$JVM_KEY"}`;
  echo -n "Result; ";
  if [ "$RESULT" = "0" ]; then
    echo -n "Success";
  else
    echo -n "Failure";
    EXIT=1;
  fi;
  echo " in jvm: $JVM (status:$RESULT)";
done;

echo "All done.";

# EOF
exit $EXIT;