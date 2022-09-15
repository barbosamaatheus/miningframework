package services.outputProcessors.soot.arguments

import groovy.cli.commons.CliBuilder
import groovy.cli.commons.OptionAccessor

class ArgsParser {

    private CliBuilder cli
    private OptionAccessor options

    ArgsParser() {
        this.cli = new CliBuilder(usage: "miningframework [options]",
                header: "the Mining Framework take an input csv file and a name for the output dir (default: output) \n Options: ")

        defParameters()
    }

    private defParameters() {
        this.cli.h(longOpt: 'help', 'Show help for executing commands')
        this.cli.a(longOpt: 'allanalysis', 'Excute all analysis')
        this.cli.t(longOpt: 'timeout', args: 1, argName: 'timeout', "timeout (default: 240)")
        this.cli.df(longOpt: 'svfa-intraprocedural',  "Run svfa-intraprocedural")
        this.cli.idf(longOpt: 'svfa-interprocedural',  "Run svfa-interprocedural")
        this.cli.cf(longOpt: 'dfp-confluence-intraprocedural',  "Run dfp-confluence-intraprocedural")
        this.cli.icf(longOpt: 'dfp-confluence-interprocedural',  "Run dfp-confluence-interprocedural")
        this.cli.oa(longOpt: 'overriding-intraprocedural',  "Run overriding-intraprocedural")
        this.cli.ioa(longOpt: 'overriding-interprocedural',  "Run overriding-interprocedural")
        this.cli.dfp(longOpt: 'dfp-intra',  "Run dfp-intra")
        this.cli.idfp(longOpt: 'dfp-inter',  "Run dfp-inter")
        this.cli.cd(longOpt: 'cd',  "Run cd")
        this.cli.cde(longOpt: 'cde',  "Run cd-e")
        this.cli.pdg(longOpt: 'pdgsdg',  "Run pdg-sdg")
        this.cli.pdge(longOpt: 'pdgsdge',  "Run pdg-sdg-e")
    }

    Arguments parse(args) {
        this.options = this.cli.parse(args)
        Arguments resultArgs = new Arguments()

        parseOptions(resultArgs)

        return resultArgs
    }

    void printHelp() {
        this.cli.usage()
    }


    private void parseOptions(Arguments args) {

        if (this.options.h) {
            args.setIsHelp(true)
        }

        if (this.options.a) {
            args.setAllanalysis(true)
        }

        if (this.options.t) {
            args.setTimeout(this.options.t.toInteger())
        }

        if (this.options.df) {
            args.setAllanalysis(false)
            args.setDfIntra(true)
        }

        if (this.options.idf) {
            args.setAllanalysis(false)
            args.setDfInter(true)
        }


        if (this.options.cf) {
            args.setAllanalysis(false)
            args.setCfIntra(true)
        }

        if (this.options.icf) {
            args.setAllanalysis(false)
            args.setCfInter(true)
        }

        if (this.options.oa) {
            args.setAllanalysis(false)
            args.setOaIntra(true)
        }

        if (this.options.ioa) {
            args.setAllanalysis(false)
            args.setOaInter(true)
        }


        if (this.options.dfp) {
            args.setAllanalysis(false)
            args.setDfpIntra(true)
        }

        if (this.options.idfp) {
            args.setAllanalysis(false)
            args.setDfpInter(true)
        }


        if (this.options.cd) {
            args.setAllanalysis(false)
            args.setCd(true)
        }

        if (this.options.cde) {
            args.setAllanalysis(false)
            args.setCde(true)
        }
        if (this.options.pdg) {
            args.setAllanalysis(false)
            args.setPdgSdg(true)
        }
        if (this.options.pdge) {
            args.setAllanalysis(false)
            args.setPdgSdge(true)
        }
    }
}