namespace java com.aries.hera.contract.thrift.service

include "dto.thrift"

//服务
service DiscoverService {

   //用于检测client-server之间通讯是否正常
   string ping(),

   list<dto.ServiceInfo> getServiceList(1:string servicename)

   i16 registe(1:dto.ServiceInfo serviceInfo)

   bool cancel(1:dto.ServiceInfo serviceInfo)
}