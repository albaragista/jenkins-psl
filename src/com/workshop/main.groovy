#!/usr/bin/groovy
package com.workshop
 
import com.workshop.Config
import com.workshop.stages.*
 
def main(script) {
   // Object initialization
   c = new Config()
   sprebuild = new prebuild()
   sbuild = new build()
   spostbuild = new postbuild()
   sdeploy = new deploy()
   spostdeploy = neew postdeploy()

 
   // Pipeline specific variable get from injected env
   // Mandatory variable will be check at details & validation steps
   def repository_name = ("${script.env.repository_name}" != "null") ? "${script.env.repository_name}" : ""
   def branch_name = ("${script.env.branch_name}" != "null") ? "${script.env.branch_name}" : ""
   def git_user = ("${script.env.git_user}" != "null") ? "${script.env.git_user}" : ""
   def docker_user = ("${script.env.docker_user}" != "null") ? "${script.env.docker_user}" : ""
   def app_port = ("${script.env.app_port}" != "null") ? "${script.env.app_port}" : ""
   def pr_num = ("${script.env.pr_num}" != "null") ? "${script.env.pr_num}" : ""
   def dockerTool = tool name: 'docker',type: 'dockerTool'
   // Have default value
   def docker_registry = ("${script.env.docker_registry}" != "null") ? "${script.env.docker_registry}" : "${c.default_docker_registry}"
   // Timeout for Healtcheck
   def timeout_hc = (script.env.timeout_hc != "null") ? script.env.timeout_hc : 10

   // Pipeline object
   p = new Pipeline(
       repository_name,
       branch_name,
       git_user,
       docker_user,
       app_port,
       pr_num,
       dockerTool,
       docker_registry,
       timeout_hc
       
 
   )
 
   ansiColor('xterm') {
       stage('Pre Build - Details') {
        //    TODO: Call pre build details function
        sprebuild.validation(p)
        sprebuild.details(p)
       }
 
       stage('Pre Build - Checkout & Test') {
        //    TODO: Call pre build checkout & test function
        sprebuild.checkoutBuildTest(p)
       }
 
       stage('Build & Push Image') {
        //    TODO: Call build & push image function
        sbuild.build(p)
       }
 
       stage('Merge') {
        //    TODO: Call merge function
        spostbuild.merge(p)
       }
 
       stage('Deploy') {
        //    TODO: Call deploy function
        sdeploy.deploy(p)
       }
 
       stage('Service Healthcheck') {
        //    TODO: Call healthcheck function
        spostdeploy.healthcheck(p)
       }
   }
}
 
return this
