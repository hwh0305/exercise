package org.hao.experiment.zookeeper;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Properties;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

public class Demo {

    private ZooKeeper zk = null;

    public Demo() {
        try {
            zk = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183,localhost:2184/test", 500, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChildren(String path) {
        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children) {
                System.out.println(child);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createChildren(String path, byte[] data) {
        try {
            zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeChild(String path) {
        try {
            Stat stat = zk.exists(path, true);
            if (stat != null) {
                zk.delete(path, stat.getVersion());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public byte[] getDate(String path) {
        Stat stat;
        try {
            stat = zk.exists(path, false);
            if (stat != null) {
                return zk.getData(path, false, stat);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Demo d = new Demo();
        d.showChildren("/");
        d.createChildren("/" + InetAddress.getLocalHost().getHostAddress(), new byte[0]);
        d.showChildren("/");
        Thread.sleep(2000);
        d.close();
    }

}
