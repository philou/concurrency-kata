# -*- mode: ruby -*-
# vi: set ft=ruby :

#Check if you have the good Vagrant version to use docker provider...
Vagrant.require_version ">= 1.6.0"

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

# Use docker as default provider
ENV['VAGRANT_DEFAULT_PROVIDER'] = 'docker'

# This docker config was inspired by https://github.com/bubenkoff/vagrant-docker-example

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  # config.vm.box = "ubuntu/trusty64"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # config.vm.network "forwarded_port", guest: 80, host: 8080

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "docker" do |d|
    # The path to a directory containing a Dockerfile. One of this or
    # image is required.
    d.build_dir = "."

    # If true, then Vagrant will support SSH with the container. This
    # allows vagrant ssh to work, provisioners, etc. This defaults to
    # false.
    d.has_ssh = true
  end

  # The port to SSH into. By default this is port 22.
  config.ssh.port = 22

  # If true, X11 forwarding over SSH connections is enabled. Defaults to
  # false.
  config.ssh.forward_x11 = true

  # The path to the private key to use to SSH into the guest machine. By
  # default this is the insecure private key that ships with Vagrant, since
  # that is what public boxes use. If you make your own custom box with a
  # custom SSH key, this should point to that private key.
  # You can also specify multiple private keys by setting this to be an array.
  # This is useful, for example, if you use the default private key to
  # bootstrap the machine, but replace it with perhaps a more secure key later.
  config.ssh.private_key_path = "~/.ssh/id_rsa"

  #  If true, agent forwarding over SSH connections is enabled. Defaults to false.
  config.ssh.forward_agent = true

  # View the documentation for the provider you are using for more
  # information on available options.

  # Define a Vagrant Push strategy for pushing to Atlas. Other push strategies
  # such as FTP and Heroku are also available. See the documentation at
  # https://docs.vagrantup.com/v2/push/atlas.html for more information.
  # config.push.define "atlas" do |push|
  #   push.app = "YOUR_ATLAS_USERNAME/YOUR_APPLICATION_NAME"
  # end

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "shell", inline: <<-SHELL
    echo "Updating package definitions"
    sudo apt-get update

    echo "Installing git and java"
    sudo apt-get -y install git openjdk-7-jdk maven
    # sudo apt-get -y install openjdk-7-source openjdk-7-dbg
  SHELL

  config.vm.provision "file", source: "~/.gitconfig", destination: "$HOME/.gitconfig"

  config.vm.provision "shell", privileged: false, inline: <<-SHELL
    if [ ! -d "$HOME/.idea" ]; then
      echo "Installing IDEA"

      wget http://download.jetbrains.com/idea/ideaIC-14.1.4.tar.gz
      tar -zxvf ideaIC-14.1.4.tar.gz

      mv `find . -maxdepth 1 -type d -and -name "idea-IC-141*"` $HOME/.idea

      sed --in-place 's/-Xmx[0-9]*m/-Xmx2048m/' $HOME/.idea/bin/idea.vmoptions

      echo 'export PATH="$HOME/.idea/bin:$PATH"' >> ~/.bashrc

      mkdir --parents $HOME/.IdeaIC14/config
    fi
  SHELL

  config.vm.provision "file", source: "Vagrantfiles/intellij/git.xml", destination: "$HOME/.IdeaIC14/config/options/git.xml"
  config.vm.provision "file", source: "Vagrantfiles/intellij/jdk.table.xml", destination: "$HOME/.IdeaIC14/config/options/jdk.table.xml"
  config.vm.provision "file", source: "Vagrantfiles/intellij/project.default.xml", destination: "$HOME/.IdeaIC14/config/options/project.default.xml"
  config.vm.provision "file", source: "Vagrantfiles/intellij/mavenVersion.xml", destination: "$HOME/.IdeaIC14/config/options/mavenVersion.xml"
end
