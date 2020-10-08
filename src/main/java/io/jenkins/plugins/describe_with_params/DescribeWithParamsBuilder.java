package io.jenkins.plugins.describe_with_params;

import java.io.IOException;
import java.util.Map;
import jenkins.tasks.SimpleBuildStep;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause.UserIdCause;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class DescribeWithParamsBuilder extends Builder implements SimpleBuildStep {
    private final boolean starter;
    private final String separator;
    private final String excludes;

    public boolean getStarter() {
        return starter;
    }

    public String getSeparator() {
        return separator;
    }

    public String getExcludes() {
        return excludes;
    }    

    @DataBoundConstructor
    public DescribeWithParamsBuilder(boolean starter, String separator, String excludes) {
        super();
        this.starter = starter;
        this.separator = separator;
        this.excludes = excludes;
    }

    @Override
    public void perform(Run<?,?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        String desc = "";

        if (starter) {
            UserIdCause userIdCause = run.getCause(UserIdCause.class);
            desc = "Started by " + userIdCause.getUserName() + separator + "\n\r";
        }

        if (run instanceof AbstractBuild) {
            AbstractBuild build = (AbstractBuild)run;
        
            String[] excludesArr = excludes.split(";");
            Map<String, String> vars = build.getBuildVariables();
            for (Map.Entry<String, String> entry : vars.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();

                boolean found = false;
                for (int i = 0; i < excludesArr.length; i++) {
                    if (excludesArr[i].equals(key)) {
                        found = true;
                        break;
                    }
                }
            
                if (!found) {
                    desc = desc + key + ": " + value + separator + "\n\r";
                }
            }            
        } else {
            desc = desc + "getBuildVariables failed" + separator + "\n\r";
        }

        //run.setDescription(Jenkins.getActiveInstance().getMarkupFormatter().translate(desc));
        run.setDescription(desc);
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> item) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Describe with params";
        }

    }

}
