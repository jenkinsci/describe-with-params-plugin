# Describe With Params Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/describe-with-params-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/describe-with-params-plugin/job/master/)
[![Contributors](https://img.shields.io/github/contributors/jenkinsci/describe-with-params-plugin.svg)](https://github.com/jenkinsci/describe-with-params-plugin/graphs/contributors)
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/describe-with-params.svg)](https://plugins.jenkins.io/describe-with-params)
[![GitHub release](https://img.shields.io/github/release/jenkinsci/describe-with-params-plugin.svg?label=changelog)](https://github.com/jenkinsci/describe-with-params-plugin/releases/latest)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/describe-with-params.svg?color=blue)](https://plugins.jenkins.io/describe-with-params)

## Introduction

Set the build description with parameters and starter, like this:

![build-history-desc.png](images/build-history-desc.png)

## Usage

### For freeStyle job

Add it to the build step, in your job configuration page. And setting it.

![add-build-step](images/add-build-step.png)
![plugin-setting](images/plugin-setting.png)

### For pipeline job
```
step([$class: 'DescribeWithParamsBuilder', starter: 'true', separator: '', excludes: ''])
```

## Issues

Report issues and enhancements in the [Jenkins issue tracker](https://issues.jenkins-ci.org/).

## Contributing

[contribution guidelines](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md)

## LICENSE

Licensed under MIT, see [LICENSE](LICENSE.md)

