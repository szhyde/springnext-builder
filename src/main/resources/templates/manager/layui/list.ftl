<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head th:include="layout/templates :: head('${po.remark}管理','${po.remark}管理')">
<meta charset="utf-8" />
</head>

<body class="layui-anim layui-anim-up">
	<div th:replace="layout/templates :: nav('${projectInfo.menuModelName}','${po.remark}管理')"></div>


	<div class="x-body">
		<div class="layui-row">
			<form class="layui-form layui-col-md12 x-so">
			<#list po.fields as field>
				<input type="text" name="search_LIKE_${field.name}" style="width: 100px;" placeholder="${field.remark}" autocomplete="off" class="layui-input"> 
			</#list>
				<button class="layui-btn" lay-submit="" lay-filter="formSearch">
					<i class="layui-icon">&#xe615;</i>
				</button>
			</form>
		</div>
		<xblock>
		<button class="layui-btn" onclick="add()">
			<i class="layui-icon">&#xe61f;</i>
		</button>

		<button class="layui-btn layui-btn-danger" onclick="batchDelete()">
			<i class="layui-icon">&#xe640;</i>
		</button>


		<span class="x-right" style="line-height: 40px;"></span> </xblock>
		<div class="layui-row">
			<table class="layui-table" lay-filter="main_table" 
				lay-data="{id: 'main_table',url:'./queryList', page:true,height:'full-197',cellMinWidth:120}">
				<thead>
					<tr>
						<th lay-data="{type:'checkbox', fixed: 'left'}"></th>
						<#list po.fields as field>
						<th lay-data="{field:'${field.name}', align:'center'}">${field.remark}</th>
						</#list>
						<th lay-data="{align:'center', toolbar:'#tableBar', minWidth:160, fixed:'right'}">操作</th>
					</tr>
				</thead>
			</table>
			<script type="text/html" id="tableBar">
			<a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="set"><i class="layui-icon">&#xe614;</i></a>
			<a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon">&#xe642;</i></a>
			<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon">&#xe640;</i></a>
    	</script>

		</div>
	</div>
	<script th:src="@{/assets/lib/layui/layui.all.js}" charset="utf-8"></script>
	<script type="text/javascript" th:src="@{/assets/js/xadmin.js}"></script>
	<script type="text/javascript">
		var crudWidth = 455;
		var crudHeight = 260;
	</script>
	<script type="text/javascript" th:src="@{/assets/js/crud.js}"></script>
</body>
</html>
