#!/bin/sh
echo =================================
echo  �Զ�������ű�����
echo =================================

echo ֹͣԭ�������еĹ���
APP_NAME=helloworld

tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 2
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi

echo ׼����Git�ֿ���ȡ���´���
cd /usr/local/helloworld

echo ��ʼ��Git�ֿ���ȡ���´���
git pull
echo ������ȡ���

echo ��ʼ���
output=`mvn clean package -Dmaven.test.skip=true`

cd target

echo ������Ŀ
nohup java -jar helloworld-1.0-SNAPSHOT.jar &> helloworld.log &
echo ��Ŀ�������
