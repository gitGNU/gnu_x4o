#!/bin/sh
#
# Copyright (c) 2004-2014, Willem Cazander
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
#MVN=mvn;
MVN=~/bin/mvn3/apache-maven-3.0.3/bin/mvn;
MVN_ARG="clean test";
JVMS="
  /usr/lib/jvm/java-6-openjdk-amd64/
  /usr/lib/jvm/java-7-openjdk-amd64/
  /usr/lib/jvm/jdk-6-oracle-x64/
  /usr/lib/jvm/jdk-7-oracle-x64/
  /usr/lib/jvm/jdk1.5.0_22/
";
# Not working  /usr/lib/jvm/java-1.5.0-gcj-4.8-amd64/

# Goto project root;
cd `dirname $0`/../../..;

# Test maven version
MVN_VERSION=`mvn -v | grep Apache | awk '{print $3}'`;
if [ "$MVN_VERSION" = "3.0.2" ]||[ "$MVN_VERSION" = "3.0.1" ]||[ "$MVN_VERSION" = "3.0.0" ]; then
	echo "Need as least version 3.0.3 or higher because of java6 bytecode";
	exit 1;
fi;

# Print meta data
echo "Starting jvm tests";
echo "Maven command:  $MVN";
echo "Maven verion:   $MVN_VERSION";
echo "Maven argument: $MVN_ARG";

# Check conditions
JVMS_ORG=$JVMS;
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
  echo "";
  if [ -e $JVM ]; then
    EXE_JVM=$JVM;
    EXE_MVN_ARG=$MVN_ARG;
    case "$JVM" in
      *gcj*) EXE_JVM=`echo $JVMS_ORG|awk '{print $1}'`;EXE_MVN_ARG="$MVN_ARG -Djvm=$JVM"; ;;
      *);;
    esac
    echo "==== Running next jvm ===";
    echo "export JAVA_HOME=$EXE_JVM";
    echo "$MVN $EXE_MVN_ARG";
    echo "";
    export JAVA_HOME=$EXE_JVM;
    $MVN $EXE_MVN_ARG;
    RESULT=$?;
  else
    RESULT="JVM path not found";
  fi;
  echo "";
  export "JVM_RESULT""$JVM_KEY"="$RESULT";
done;

EXIT=0;
echo "";
echo "Test summary;";
for JVM in $JVMS; do
  JVM_KEY=`echo $JVM | sed 's/\/\|\-//g'|tr -d '.'`;
  JVM_VERSION=`$JVM/bin/java -version 2>&1 |grep "java version"|awk '{print $3}'`;
  RESULT=`eval echo \\${"JVM_RESULT""$JVM_KEY"}`;
  echo -n "Result; ";
  if [ "$RESULT" = "0" ]; then
    echo -n "Success";
  else
    echo -n "Failure";
    EXIT=1;
  fi;
  echo " in jvm: $JVM_VERSION \t$JVM (status:$RESULT)";
done;

echo "All done.";

# EOF
exit $EXIT;
