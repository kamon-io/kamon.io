module Jekyll
  module CustomFilters

    def sidebar_link(current_page, classes, expected_page)
      if current_page.eql? expected_page
        'class="' + classes + ' current-page" href="' + expected_page + '"'
      else
        'class="' + classes + '" href="' + expected_page + '"'
      end
    end

    def bintray_latest_release(module_name)
      '<a href="https://bintray.com/kamon-io/releases/'+ module_name +'/_latestVersion">' +
        '<img src="https://api.bintray.com/packages/kamon-io/releases/'+ module_name +'/images/download.svg" alt="Download">' +
      '</a>'
    end

    def maven_latest_release(module_name)
      '<img src="https://maven-badges.herokuapp.com/maven-central/io.kamon/'+ module_name +'_2.11/badge.svg" alt="Download">'
    end


  end
end

class ModuleTree < Liquid::Drop
  attr_reader :name
  attr_reader :versions
  attr_reader :current_version
  attr_reader :sections
  attr_reader :pages

  def initialize(name, versions, current_version)
    @name = name
    @versions = versions
    @current_version = current_version
    @sections = []
    @pages = []
  end

  def add(page)
    tree_path = page.url.split('/').drop(4)

    if tree_path.length() == 2 then
      section_name = tree_path[0]
      if @sections.count { |te| te.name.eql? section_name } == 0 then
        @sections.push(Section.new(section_name))
      end

      @sections
        .select { |te| te.name.eql? section_name }
        .each   { |te| te.pages.push(page) }
    else
      @pages.push(page)
    end
  end
end

class Section < Liquid::Drop
  attr_reader :name
  attr_reader :pages

  def initialize(name)
    @name = name
    @pages = []
  end
end

Liquid::Template.register_filter(Jekyll::CustomFilters)