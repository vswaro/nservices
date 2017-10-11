# nservices

<b>Executing Cloudify Rest Client using jar</b>

Prerequiste 
1. Requires Java 1.8
2. Download https://github.com/vswaro/nservices/blob/master/CloudifyRestClient.jar
3. Open cmd and goto downloaded jar folder 
4. Execute "java -jar CloudifyRestClient.jar"

```shell
<b>Cloudify Rest Client (Host:http://10.11.94.58/api/v3.1) </b>
Help : These commands are defined internally. Type 'help' or'exit'
Usage: list [options]
 	 options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups
Usage: get [options] [inputs]
 	 options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups|status|version 
 	 inputs: -s  prints short details, default prints all
Usage: executions <deploymentId> [workflowId] 
 	 worflowid: install|uninstall
>>
list deployments -s
DEPLOYMENTS LIST: deployments?_include=id&
{
  "items" : [ {
    "id" : "nodecellar-docker-deploy1"
  } ],
  "metadata" : {
    "pagination" : {
      "total" : 1,
      "offset" : 0,
      "size" : 0
    }
  }
}
>>
executions nodecellar-docker-deploy1 uninstall
EXECUTIONS: executions
{
  "status" : "pending",
  "parameters" : {
    "ignore_failure" : false
  },
  "is_system_workflow" : false,
  "blueprint_id" : "nodecellar-docker",
  "tenant_name" : "default_tenant",
  "created_at" : "2017-10-11T10:41:47.829Z",
  "created_by" : "admin",
  "private_resource" : false,
  "workflow_id" : "uninstall",
  "error" : "",
  "deployment_id" : "nodecellar-docker-deploy1",
  "id" : "953f7e7a-0fb8-4af4-9899-a5f5c0595a3f"
}
>>
get executions 953f7e7a-0fb8-4af4-9899-a5f5c0595a3f
executionsGET: executions?id=953f7e7a-0fb8-4af4-9899-a5f5c0595a3f&
{
  "items" : [ {
    "status" : "started",
    "parameters" : {
      "ignore_failure" : false
    },
    "is_system_workflow" : false,
    "blueprint_id" : "nodecellar-docker",
    "tenant_name" : "default_tenant",
    "created_at" : "2017-10-11T10:41:47.829Z",
    "created_by" : "admin",
    "private_resource" : false,
    "workflow_id" : "uninstall",
    "error" : "",
    "deployment_id" : "nodecellar-docker-deploy1",
    "id" : "953f7e7a-0fb8-4af4-9899-a5f5c0595a3f"
  } ],
  "metadata" : {
    "pagination" : {
      "total" : 1,
      "offset" : 0,
      "size" : 0
    }
  }
}
>>
get executions 953f7e7a-0fb8-4af4-9899-a5f5c0595a3f
executionsGET: executions?id=953f7e7a-0fb8-4af4-9899-a5f5c0595a3f&
{
  "items" : [ {
    "status" : "started",
    "parameters" : {
      "ignore_failure" : false
    },
    "is_system_workflow" : false,
    "blueprint_id" : "nodecellar-docker",
    "tenant_name" : "default_tenant",
    "created_at" : "2017-10-11T10:41:47.829Z",
    "created_by" : "admin",
    "private_resource" : false,
    "workflow_id" : "uninstall",
    "error" : "",
    "deployment_id" : "nodecellar-docker-deploy1",
    "id" : "953f7e7a-0fb8-4af4-9899-a5f5c0595a3f"
  } ],
  "metadata" : {
    "pagination" : {
      "total" : 1,
      "offset" : 0,
      "size" : 0
    }
  }
}
>>
help
Help : These commands are defined internally. Type 'help' or'exit'
Usage: list [options]
 	 options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups
Usage: get [options] [inputs]
 	 options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups|status|version 
 	 inputs: -s  prints short details, default prints all
Usage: executions <deploymentId> [workflowId] 
 	 worflowid: install|uninstall
>>
list blueprints -s
BLUEPRINTS LIST: blueprints?_include=id&
{
  "items" : [ {
    "id" : "nodecellar"
  }, {
    "id" : "helloworld"
  }, {
    "id" : "nodecellar-docker"
  } ],
  "metadata" : {
    "pagination" : {
      "total" : 3,
      "offset" : 0,
      "size" : 0
    }
  }
}
>>
```
