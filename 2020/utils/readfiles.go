package utils

import (
	"os"
	"strings"
)

func ReadFile(path string) []string {
	file, err := os.ReadFile(path)
	if err != nil {
		panic(err)
	}
	return Filter(strings.Split(string(file), "\n"), func(str string) bool {
		return len(strings.Trim(str, " ")) > 0
	})
}
