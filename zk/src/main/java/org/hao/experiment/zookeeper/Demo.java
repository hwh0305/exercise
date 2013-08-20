package org.hao.experiment.zookeeper;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Demo {

    private ZooKeeper zk = null;

    public Demo() {
        try {
            zk = new ZooKeeper(
                               "10.16.42.77:2181,10.16.42.77:2182,10.16.42.77:2183,10.16.42.77:2184,10.16.42.59:2181/hao/experiment",
                               500, null);
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
        Thread.sleep(5000);
        d.close();
    }

}
