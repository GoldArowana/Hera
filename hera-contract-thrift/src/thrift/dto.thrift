namespace java com.aries.hera.contract.thrift.dto

struct ServiceInfo {
  1: string name,
  2: string host,
  3: i32 port
}
enum RegistCode{
    SUCCESS = 1,
    NOT_CHANGE = 0,
    FAILED = -1,
}