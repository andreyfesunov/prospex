{
  description = "prospex devshell";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
      in {
        devShells.default = pkgs.mkShell {
          name = "prospex-devshell";
          buildInputs = [
            pkgs.gradle
            pkgs.openjdk
            pkgs.fish
          ];

          shellHook = ''
            echo "Welcome to the prospex development shell!"
            echo "Gradle version: $(gradle --version | head -n 3 | tail -n 1)"
            echo "Java version: $(java -version 2>&1 | head -n 1)"
            exec fish
          '';
        };
      }
    );
}

