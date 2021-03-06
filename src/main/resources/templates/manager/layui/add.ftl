<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
	<head th:include="layout/templates :: head('增加${po.remark}','增加${po.remark}')">
		<meta charset="utf-8" />
	</head>

   <div class="x-body layui-anim layui-anim-up">
        <form class="layui-form">
        	<#list po.fields as field>
          <div class="layui-form-item">
              <label class="layui-form-label">
                  ${field.remark}
              </label>
              <div class="layui-input-inline">
                  <input type="text" id="${field.name}" name="${field.name}" required lay-verify="required"
                  autocomplete="off" class="layui-input">
              </div>
              <div class="layui-form-mid layui-word-aux">
                  <span class="x-red">*</span>
              </div>
          </div>
          </#list>
          <div class="layui-form-item">
              <label for="L_repass" class="layui-form-label">
              </label>
              <button  class="layui-btn" lay-filter="add" lay-submit="">
                  增加
              </button>
          </div>
      </form>
    </div>
	<script th:src="@{/assets/lib/layui/layui.all.js}" charset="utf-8"></script>
	<script type="text/javascript" th:src="@{/assets/js/xadmin.js}"></script>
	<script>
          var form = layui.form
          ,layer = layui.layer;
        
          //监听提交
          form.on('submit(add)', function(data){
            $.ajax({
    			type : "post",
    			url : "./save",
    			data : data.field,
    			success : function(json){
    				console.info(json);
    				console.info(json.status);
    				if(json.success == true){
    					layer.alert("增加成功", {icon: 6},function () {
    						x_admin_close();
    		            });
    				}else{
    					layer.msg(json.message);
    				}
    			},
    			error:function(json){
    				layer.msg("服务器异常，请稍后重试");
    			}
    		});
            
            return false;
          });
          
          
    </script>
	</body>
</html>
