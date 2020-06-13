FROM tomcat:latest


USER root
WORKDIR /usr/local/
#RUN yum install -y wget
RUN wget -c https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar zxvf apache-maven-3.6.3-bin.tar.gz

#RUN yum install -y git
RUN mkdir /root/.ssh/
RUN echo "${SSH_PRIVATE_KEY}" > /root/.ssh/id_rsa

RUN git clone https://github.com/jeremy8250/helloworld.git

ENV MAVEN_HOME=/usr/local/apache-maven-3.6.3
ENV PATH=$PATH:$MAVEN_HOME/bin:$JAVA_HOME/bin

RUN rm -rf /usr/local/tomcat/webapps/ROOT
RUN rm -rf /usr/local/tomcat/webapps/ROOT.war

ADD target/helloworld.war /usr/local/tomcat/webapps

EXPOSE 8080

CMD ["/usr/local/tomcat/bin/startup.sh","run"]

