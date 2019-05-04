# spring-cloud-task-as-k8s-cronjob

## [Spring Cloud Task] Application running as [CronJob] in Kubernetes

A Spring Cloud Task Application which runs as CronJob in Kubernetes every minute to fetch BitCoin rates, saves the rates in Mysql and notifies Users if/when the threshold is reached.

### Installation and Configuration

##### Pre-requisite:
MySql Server is up and running and application is configured the MySql credentials
 
##### Clone repo:
```sh
$ git clone https://github.com/RawSanj/spring-cloud-task-as-k8s-cronjob.git
```

#### Build and Run the applications:

Build and run the **spring-cloud-task-as-k8s-cronjob** application:
```sh
$ cd spring-cloud-task-as-k8s-cronjob

$ mvn clean package

$ mvn spring-boot:run
```

### Run in Kubernetes

#### Assuming you have a Kubernetes Cluster up and running and kubectl is configured, run:

```sh
$ kubectl apply -f src/main/k8s
```

#### Or try [Play with Kubernetes] to quickly setup a K8S cluster:
1. Follow the instructions to create Kubernetes cluster.
2. Install git by running:

    `$ yum install git -y`
3. Clone the repository:
  
    `$ git clone https://github.com/RawSanj/spring-cloud-task-as-k8s-cronjob.git`
4. Update Kubernetes Secrets under `src/main/k8s/cronjob.yaml` file.
5. Run *spring-cloud-task-as-k8s-cronjob* as Kubernetes CronJob. CronJob Pods connects to MySql server and stores Bitcoin rates every minute.     

    `$ cd spring-cloud-task-as-k8s-cronjob`
    
    `$ kubectl apply -f src/main/k8s`
6. Clone and deploy the UI Application - [spring-cronjob-currency] by running `kubectl apply -f src/main/k8s`


License
----

Apache License 2.0

Copyright (c) 2018 Sanjay Rawat


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [Play with Kubernetes]: https://labs.play-with-k8s.com
   [Spring Cloud Task]: https://spring.io/projects/spring-cloud-task
   [CronJob]: https://kubernetes.io/docs/concepts/workloads/controllers/cron-jobs/
   [spring-cronjob-currency]: https://github.com/RawSanj/spring-cronjob-currency

