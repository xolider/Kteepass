# Contributing to Kteepass

Welcome and thank you for considering contributing to Kteepass library !
Please read and follow these guidelines to keep Kteepass as simple and maintainable as possible. Don't forget that your 
developments may have impacts to other softwares using this library.

## Summary

* [Prerequisites](#prerequisites)
* [Getting Started](#getting-started)
  * [Issues](#issues)
  * [Pull requests](#pull-requests)

## Prerequisites

Kteepass is a library based on the Keepass standards. This is much more theoretical than practical. Standards are based on 
the KDB/KDBX file specifications and Keepass recommendations. Considering this, please take a look at the [KeePass Help Center](https://keepass.info/help/base/index.html).

The most interesting and pertinent sections are about the [KDBX File Format](https://keepass.info/help/kb/kdbx.html) and the "Features" 
category.

## Getting Started

First of all, please consider using Jetbrains Intellij Idea as the first IDE to contribute to this project. The primary 
reason is that it is the one that supports the best the in-place project structure and tech.

Kteepass uses Kotlin Multiplatform project architecture to provide the library for many platforms. Be sure you 
understand this kind of architecture before making any breaking changes.

### Issues

You can use Issues to report any problems with the library or even request new features. No template is yet provided, but try 
to be as clear as possible to make easier to others to understand what you mean.

If possible, try to share or attach a reproducible example of the problem you're having. It often takes a shorter time 
to identify the source problem.

### Pull requests

PRs are welcome to fix any open (or non-open) issues in the code, but not only. Contributions to Actions Workflows files 
are also welcome, as well as, updating the [README](https://github.com/xolider/Kteepass/blob/main/README.MD) or configuration
files.

Kteepass follows the "fork-and-pull" workflow to make contributions:

1. Fork the repository to your own account
2. Create a new branch from the `main` and give it a clear name
3. Commit your developments with clear messages describing in few words what it is supposed to change
4. Create a pull request targeting the repo's `main` branch and enter a better description if needed

Pull requests need at least 1 review from an admin to be merged. Also, only squash merges are allowed to keep history simple