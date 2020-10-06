package io.jenkins.plugins.describe_with_params;

import java.io.IOException;
import java.util.Map;
import org.jenkinsci.Symbol;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Cause.UserIdCause;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;

public class DescribeWithParamsBuilder extends Builder {

    public DescribeWithParamsBuilder() {
        super();
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        UserIdCause userIdCause = build.getCause(UserIdCause.class);
        String desc = "Started by " + userIdCause.getUserName() + "\n\r";

        Map<String,String> vars = build.getBuildVariables();
        for (Map.Entry<String, String> entry : vars.entrySet())
        {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();

            desc = desc + mapKey + ": " + mapValue + "\n\r";
        }
        build.setDescription(desc);

        return true;
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> item) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "describe with params";
        }

    }

}
