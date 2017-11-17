package com.wjh.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-spring.xml")
public class TestActiviti {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	ProcessEngineFactoryBean processEngine;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private IdentityService identityService;

	@Test
	public void testEvent() throws InterruptedException {

		//deployProcess();
		
		/*List<String> auditUsers = new ArrayList<>();
		auditUsers.add("大雄");
		auditUsers.add("静香");
		auditUsers.add("哆啦A梦");
		auditUsers.add("胖虎");
		auditUsers.add("小夫");*/
		 
		Map<String, Object> businessVariables = new HashMap<>();
		//businessVariables.put("auditUsersList", auditUsers);
		
		businessVariables.put("adoptCounter", 1);
		
		
		//Task task = taskService.createTaskQuery().processInstanceId("55009").list().get(0);
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("胖虎").list();
		Task task = tasks.get(0);
		completeTask(task.getId(), businessVariables);
		
		
		/*List<Task> tasks = taskService.createTaskQuery().taskAssignee("fatherWan").list();
		for (Task task : tasks) {
			System.out.println(task.getName() + " : " + task.getAssignee());

			taskService.claim(task.getId(), "fatherWan");
		}

		tasks = taskService.createTaskQuery().taskAssignee("fatherWan").list();
		for (Task task : tasks) {
			taskService.complete(task.getId());
			System.out.println("完成任务" + task.getName() + " : " + task.getId() + " completed ");
		}

		HistoricProcessInstance hpInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		System.out.println("end time: " + hpInstance.getEndTime());*/

	}

	private void completeTask(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

	// 部署流程
	private void deployProcess() {
		repositoryService.createDeployment().addClasspathResource("com/wjh/diagrams/test.bpmn").deploy();

		System.out.println("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());

		identityService.setAuthenticatedUserId("Fanther Wan");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testCountersign");
		System.out.println("流程实例ID：" + processInstance.getId());
		System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());
		System.out.println("流程定义key：" + processInstance.getProcessDefinitionKey());
		System.out.println("部署成功");
	}

}
