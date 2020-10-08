package io.jenkins.plugins.describe_with_params;

import java.io.IOException;
import java.util.Map;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Cause.UserIdCause;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class DescribeWithParamsBuilder extends Builder {
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
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        String desc = "";

        if (starter) {
            UserIdCause userIdCause = build.getCause(UserIdCause.class);
            desc = "Started by " + userIdCause.getUserName() + separator + "\n\r";
        }

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

        //build.setDescription(Jenkins.getActiveInstance().getMarkupFormatter().translate(desc));
        build.setDescription(desc);

        return true;
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
