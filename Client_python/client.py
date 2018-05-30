# -*- coding: utf-8 -*-
import socket
import re


serverAddr = '192.168.3.101'
serverPort = 5000

def initConnection():
    global serverAddr, serverPort
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((serverAddr, serverPort))
    return s
    

def sendMsg(sock, msg):
    sock.send(bytes(msg, 'utf-8'))


def recvResponse(sock):
    cnk = []
    c = b' '
    while c != '\n':
        c = sock.recv(1).decode('utf-8')
        cnk.append(c)
    return ''.join(cnk)


def login(sock, username):
    sendMsg(sock, 'LOGIN: {}\n'.format(username))
    resp = recvResponse(sock)
    print(resp)


def connectToPeer(ip):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return s

def sendUDP(ip, data, port=6001):
    s = connectToPeer(ip)
    s.sendto(bytes(data, 'utf-8'), (ip, port))
    closePeer(s)

def closePeer(sock):
    sock.close()

    
def chat(sock, username, data):
    sendMsg(sock, 'SENDTO: {}\n'.format(username))
    resp = recvResponse(sock)
    if 'ERROR:' in resp:
        print('User {} wasn\'t found')
    else:
        p = re.compile(r'[^\d]*(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})')
        m = p.match(resp)
        if m is not None:
            ip = m.group(1)
            print(ip)
            sendUDP(ip, ' ')
            sendUDP(ip, 'aSAs'*250)

def closeConnection(sock):
    sock.close()