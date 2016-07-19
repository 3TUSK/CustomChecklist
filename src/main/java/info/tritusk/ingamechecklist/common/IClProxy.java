package info.tritusk.ingamechecklist.common;

import java.io.File;

import org.apache.logging.log4j.Logger;

import info.tritusk.ingamechecklist.common.command.CommandTask;
import info.tritusk.ingamechecklist.common.config.ConfigMain;
import info.tritusk.ingamechecklist.common.task.TaskEntryLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class IClProxy {
	
	public static Logger log;
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		File mainDir = new File(event.getModConfigurationDirectory(), "CustomChecklist");
		if (!mainDir.exists() || !mainDir.isDirectory())
			mainDir.mkdir();
		ConfigMain.initConfig(new File(mainDir, "CustomChecklist.cfg"));
		ConfigMain.initGlobalTasks(new File(mainDir, "GlobalTasks.xml"));
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		if (!TaskEntryLoader.xmlHandlerInitialized)
			TaskEntryLoader.initXMLHandler();
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTask());
		TaskEntryLoader.loadGlobalChecklist(ConfigMain.globalTasks);
		log.info("Loaded local checklist.");
	}
	
	public void onServerStopping(FMLServerStoppingEvent event) {
		TaskEntryLoader.saveGlobalChecklist(TaskEntryLoader.localEntryList);
	}

}
