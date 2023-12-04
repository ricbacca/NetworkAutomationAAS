#!/user/bin/python
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.node import Node
from mininet.node import Host
from mininet.link import TCLink
from mininet.link import Intf
from mininet.log import setLogLevel, info
from mininet.cli import CLI
from mininet.node import Controller
from mininet.node import RemoteController
from mininet.util import quietRun

def myNetwork():
    info( 'Creating empty network..\n' )
    net = Mininet(topo=None, build=False, link=TCLink)
    sw1 = net.addSwitch('sw1')
    sw2 = net.addSwitch('sw2')
    # Adding hosts
    h1 = net.addHost('host1', ip='10.0.0.1')
    h2 = net.addHost('host2', ip='10.0.0.2')
    h3 = net.addHost('host3', ip='10.0.0.3')    
    h4 = net.addHost('host4', ip='10.0.0.4')
    h5 = net.addHost('host5', ip='10.0.0.5')
    h6 = net.addHost('host6', ip='10.0.0.6')
    # Connecting hosts to switches and switch to switch
    net.addLink(h1, sw1)
    net.addLink(h2, sw1)
    net.addLink(h3, sw1)

    net.addLink(h4, sw2)
    net.addLink(h5, sw2)
    net.addLink(h6, sw2)

    net.addLink(sw1, sw2)

    h1.setMAC("00:00:00:00:00:01", h1.name + "-eth0")
    h2.setMAC("00:00:00:00:00:02", h2.name + "-eth0")
    h3.setMAC("00:00:00:00:00:03", h3.name + "-eth0")
    h4.setMAC("00:00:00:00:00:04", h4.name + "-eth0")
    h5.setMAC("00:00:00:00:00:05", h5.name + "-eth0")
    h6.setMAC("00:00:00:00:00:06", h6.name + "-eth0")

    # Connecting switches to external controller
    net.addNAT().configDefault()
    net.start()
    sw1.cmd('ovs-vsctl set-controller ' +  sw1.name + ' tcp:127.0.0.1:6633')
    sw2.cmd('ovs-vsctl set-controller ' +  sw2.name + ' tcp:127.0.0.1:6653')
    CLI(net)
    net.stop()

#Main
if __name__ == '__main__':
    setLogLevel('info')
    myNetwork()
