FROM ubuntu:20.04

RUN apt update -y
RUN apt install -y software-properties-common
RUN add-apt-repository -y ppa:deadsnakes/ppa

RUN apt-get install -y \
    gcc \
    python3-pip \ 
    libffi-dev \
    libssl-dev \
    libxml2-dev \
    libxslt1-dev \
    build-essential \
    openssh-client \
    openssh-server \
    openssh-sftp-server \
    curl \
    iptables \
    iputils-ping \
    iputils-tracepath \
    iproute2 \
    less \
    links \
    make \
    man-db \
    mtr \
    nano \
    ndisc6 \
    net-tools \
    netbase \
    netcat-openbsd \
    pkg-config \
    radvd \
    rsync \
    screen \
    sed \
    tar \
    tcpdump \
    telnet \
    tmux \
    traceroute \
    wget \
    vim

RUN pip3 install \
    ryu \
    gunicorn==20.1.0 \
    eventlet==0.30.2

