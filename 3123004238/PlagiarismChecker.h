#ifndef PLAGIARISMCHECKER_H
#define PLAGIARISMCHECKER_H

#include <string>
#include <vector>
#include <map>

class PlagiarismChecker {
public:
	// 构造函数：初始化原文和抄袭版论文的路径
	PlagiarismChecker(const std::string& origFile, const std::string& plagFile);

	// 计算相似度并输出结果
	void calculateAndSaveSimilarity(const std::string& outputFile);

private:
	// 读取文件内容（支持UTF-8编码）
	std::string readFile(const std::string& filePath);

	// 将文本按字符分词
	std::vector<std::string> tokenize(const std::string& text);

	// 计算词频（TF）
	std::map<std::string, int> computeTF(const std::vector<std::string>& words);

	// 计算逆文档频率（IDF）
	std::map<std::string, double> computeIDF(const std::vector<std::vector<std::string>>& documents);

	// 计算TF-IDF向量
	std::vector<double> computeTFIDF(const std::vector<std::string>& words,
		const std::map<std::string, int>& tf,
		const std::map<std::string, double>& idf);

	// 计算余弦相似度
	double cosineSimilarity(const std::vector<double>& vec1, const std::vector<double>& vec2);

	// 原文和抄袭版论文的内容
	std::string origText;
	std::string plagText;
};

#endif // PLAGIARISMCHECKER_H