//package com.aries.hera.core;
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.framework.recipes.cache.NodeCache;
//import org.apache.curator.framework.recipes.cache.PathChildrenCache;
//import org.apache.curator.framework.recipes.cache.TreeCache;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.apache.zookeeper.data.Stat;
//
//
///**
// * curator测试crud操作
// */
//public class CuratorClientTest {
//
//    /**
//     * Zookeeper info 集群用,隔开，如192.168.9.127:2181,192.168.9.128:2181,192.168.9.129:2181
//     */
//    private static final String ZK_ADDRESS = "10.50.236.129:2181";
//    /**
//     * 创建路径
//     */
//    private static final String ZK_PATH_PARENT = "/new1";
//
//    private static final String ZK_PATH = "/new1/mytest";
//
//
//    public static void main(String[] args) throws Exception {
//
//        CuratorFramework client = getClient();
//
//        /*
//        开始连接
//         */
//        client.start();
//
////        initNodeCache(client);
//
////        initPathChildrenCache(client);
//
//        initTreeCache(client);
//
//        createNode(client);
//
//        /*
//        普通查询
//         */
//        byte[] bytes = client.getData().forPath(ZK_PATH);
//        System.out.println(new String(bytes));
//        /*
//        包含状态查询
//         */
//        Stat stat = new Stat();
//        byte[] bytes1 = client.getData().storingStatIn(stat).forPath(ZK_PATH);
//        System.out.println(new String(bytes1));
//
////        updateNode(client);
//
////        deleteNode(client);
//
//        Thread.sleep(15000);
//
//        /*
//        关闭连接
//         */
//        client.close();
//    }
//
//    /**
//     * 删除节点
//     *
//     * @param client
//     * @throws Exception
//     */
//    public static void deleteNode(CuratorFramework client) throws Exception {
//       /*
//       删除节点
//        */
////        client.delete().forPath(ZK_PATH);
//       /*
//        删除节点 并且递归删除子节点
//        */
////        client.delete().deletingChildrenIfNeeded().forPath(ZK_PATH);
//        client.delete().guaranteed().forPath(ZK_PATH);
//    }
//
//    /**
//     * 更新节点内容
//     */
//    public static void updateNode(CuratorFramework client) throws Exception {
//       /*
//        更新节点信息 如果未传入version参数，那么更新当前最新版本
//        */
//        client.setData().forPath(ZK_PATH, "新内容3".getBytes());
//
//        /*
//         指定版本更新 版本不一直异常信息：org.apache.zookeeper.KeeperException$BadVersionException: KeeperErrorCode = BadVersion for
//         */
////        client.setData().withVersion(aversion).forPath(ZK_PATH);
//    }
//
//    /**
//     * 创建节点信息以及内容
//     *
//     * @param client
//     * @throws Exception
//     */
//    public static void createNode(CuratorFramework client) throws Exception {
//    /*
//    创建节点以及对应内容。无法递归创建节点
//     */
//        client.create().forPath(ZK_PATH + "/noCursion", "noCursion".getBytes());
////        client.create().forPath(ZK_PATH_PARENT,"noCursion".getBytes());
//
//        /*
//        递归创建节点以及对应内容
//         */
//        client.create().creatingParentsIfNeeded().forPath(ZK_PATH, "testdata".getBytes());
//
////        client.create().forPath(ZK_PATH_PARENT).
//    }
//
//    /**
//     * 获得客户端连接
//     *
//     * @return
//     */
//    public static CuratorFramework getClient() {
//    /*
//    重试策略
//     */
//        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
//        /*
//        CuratorFrameworkFactory工厂创建实例
//         */
//        return CuratorFrameworkFactory.newClient(ZK_ADDRESS, exponentialBackoffRetry);
//    }
//
//    /**
//     * 初始化TreeCache的节点监听
//     *
//     * @param client
//     * @throws Exception
//     */
//    public static void initTreeCache(CuratorFramework client) throws Exception {
//        TreeCache treeCache = new TreeCache(client, ZK_PATH_PARENT);
//        treeCache.start();
//        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
//            switch (treeCacheEvent.getType()) {
//                case NODE_ADDED:
//                    System.out.println("NODE_ADDED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
//                            + "，状态：" + treeCacheEvent.getData().getStat());
//                    break;
//                case NODE_UPDATED:
//                    System.out.println("NODE_UPDATED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
//                            + "，状态：" + treeCacheEvent.getData().getStat());
//                    break;
//                case NODE_REMOVED:
//                    System.out.println("NODE_REMOVED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
//                            + "，状态：" + treeCacheEvent.getData().getStat());
//                    break;
//                default:
//                    break;
//            }
//        });
//    }
//
//    /**
//     * 初始化节点监听
//     *
//     * @param client
//     * @throws Exception
//     */
//    public static void initNodeCache(CuratorFramework client) throws Exception {
//    /*
//     监听节点的新增、修改操作。  最后一个参数表示是否进行压缩
//   */
//        NodeCache nodeCache = new NodeCache(client, ZK_PATH_PARENT, false);
//        nodeCache.start(true);
//        /*
//         会监听父节点的创建和修改，删除不会监听
//         */
//        nodeCache.getListenable().addListener(() -> {
//            System.out.println("nodeCache listen begin");
//            System.out.println("data:" + nodeCache.getCurrentData().getData().toString());
//            System.out.println("nodeCache listen end");
//        });
//    }
//
//    /**
//     * 初始化子节点监听
//     *
//     * @param client
//     * @throws Exception
//     */
//    public static void initPathChildrenCache(CuratorFramework client) throws Exception {
//        /*
//        监听子节点的新增、修改、删除操作。
//        */
//        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, ZK_PATH_PARENT, true);
//        /**
//         * 如果不填写这个参数，则无法监听到子节点的数据更新
//         如果参数为PathChildrenCache.StartMode.BUILD_INITIAL_CACHE，则会预先创建之前指定的/super节点
//         如果参数为PathChildrenCache.StartMode.POST_INITIALIZED_EVENT，效果与BUILD_INITIAL_CACHE相同，只是不会预先创建/super节点
//         参数为PathChildrenCache.StartMode.NORMAL时，与不填写参数是同样的效果，不会监听子节点的数据更新操作
//         */
//        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
//        pathChildrenCache.getListenable().addListener((curatorFramework, event) -> {
//            switch (event.getType()) {
//                case CHILD_ADDED:
//                    System.out.println("CHILD_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
//                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
//                    break;
//                case CHILD_UPDATED:
//                    System.out.println("CHILD_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
//                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
//                    break;
//                case CHILD_REMOVED:
//                    System.out.println("CHILD_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
//                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
//                    break;
//                default:
//                    break;
//            }
//        });
//    }
//}
