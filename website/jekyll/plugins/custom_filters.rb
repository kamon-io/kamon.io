module Jekyll
  module CustomFilters
    def extract_module_name(input)
      input.split('/')[2]
    end

    def extract_module_versions(pages, module_name)
      pages
        .select { |page| page.url.include? module_name }
        .map    { |page| 
          puts page.url + " >>>> " + page.path + "\n"
          page 
        }
        .map    { |page| page.url.split('/')[3] }
        .select { |version| version.length > 0 }
        .uniq
    end    

    def extract_module_tree(pages, current_page_url)
      current_version = current_page_url.split('/')[3]
      module_name = current_page_url.split('/')[2]
      module_pages = pages
        .select { |page| page.url.include? module_name }

      module_versions = module_pages
        .map    { |page| page.url.split('/')[3] }
        .select { |version| version.length > 0 }
        .uniq        

      module_tree = ModuleTree.new(module_name, module_versions, current_version)
      module_url_prefix = "/documentation/" + module_name + "/" +current_version + "/"

      module_pages
        .select { |page| page.url.start_with? module_url_prefix }
        .each { |page| module_tree.add(page) }
      
      module_tree
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
    puts "Evaluating section " + tree_path[0] + "\n"

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