package org.springnext.builder;

import java.awt.EventQueue;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springnext.builder.windows.MainFrame;

/**
 * 
 * 系统启动入口
 * 
 * @author hyde
 * @see BootApplication
 * @since
 */
// SpringBoot 应用标识
@SpringBootApplication
public class BootApplication  {

	

	public static void main(String[] args) {
		//为了在运行的时候，得到spring上下文
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(BootApplication.class).headless(false)
				.run(args);

		//偷懒用lambda实现下
		EventQueue.invokeLater(() -> {
			MainFrame ex = ctx.getBean(MainFrame.class);
			ex.setVisible(true);
		});

	}

}
