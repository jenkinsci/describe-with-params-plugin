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
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
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
        StringBuilder descStr = new StringBuilder();

        if (starter) {
            UserIdCause userIdCause = run.getCause(UserIdCause.class);
            if (userIdCause != null) {
                descStr.append("Started by " + userIdCause.getUserName() + separator + "\n\r");
            }
            else {
                descStr.append("Started by unknown user" + separator + "\n\r");
            }
        }

        String[] excludesArr = excludes.split(";");

        if (run instanceof AbstractBuild) {
            AbstractBuild build = (AbstractBuild)run;

            Map<String, String> vars = build.getBuildVariables();
            for (Map.Entry<String, String> entry : vars.entrySet())
            {
                String name = entry.getKey();

                boolean found = false;
                for (int i = 0; i < excludesArr.length; i++) {
                    if (excludesArr[i].equals(name)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    descStr.append(name + ": " + entry.getValue() + separator + "\n\r");
                }
            }
        } else {
            for (ParametersAction action : run.getActions(ParametersAction.class)) {
                for (ParameterValue param : action.getAllParameters()) {
                    String name = param.getName();

                    boolean found = false;
                    for (int i = 0; i < excludesArr.length; i++) {
                        if (excludesArr[i].equals(name)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        Object value = param.getValue();
                        if (value != null) {
                            descStr.append(name + ": " + value.toString() + separator + "\n\r");
                        }
                        else {
                            descStr.append(name + ": unknown value" + separator + "\n\r");
                        }
                    }
                }
            }
        }

        //run.setDescription(Jenkins.getActiveInstance().getMarkupFormatter().translate(desc));
        run.setDescription(descStr.toString());
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
